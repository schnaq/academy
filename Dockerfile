FROM clojure:openjdk-17-tools-deps-bullseye AS shadow-build

WORKDIR /code

RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add - && \
    echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list && \
    apt update && \
    apt install -y yarn && \
    yarn global add sass

# Cache and install JavaScript dependencies
COPY package.json .
COPY yarn.lock .
COPY .yarnrc .
# COPY resources/public/node_modules/ resources/public/node_modules/
RUN yarn install

COPY deps.edn .
RUN clojure -P -M:frontend

COPY . .

RUN yarn build

# ------------------------------------------------------------------------------

FROM nginx:alpine
# Default value is robots.txt, only on other environments a custom var is needed
RUN apk add --no-cache tzdata

WORKDIR /usr/share/nginx/html
COPY --from=shadow-build /code/resources/public .
COPY nginx/schnaq.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
