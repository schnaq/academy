FROM clojure:temurin-17-alpine AS shadow-build

WORKDIR /code

RUN apk add --no-cache yarn && \
  yarn global add sass

# Cache and install JavaScript dependencies
COPY package.json .
COPY yarn.lock .
RUN yarn install

COPY deps.edn .
RUN clojure -P -M:frontend

COPY . .

RUN yarn shadow:sitemap && \
  yarn build

# ------------------------------------------------------------------------------

FROM nginx:alpine
# Default value is robots.txt, only on other environments a custom var is needed
RUN apk add --no-cache tzdata

WORKDIR /usr/share/nginx/html
COPY --from=shadow-build /code/resources/public .
COPY nginx/schnaq.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
