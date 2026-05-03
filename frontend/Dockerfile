FROM node:22-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
ARG VITE_API_URL=http://codenames.eastus.azurecontainer.io:8080
ENV VITE_API_URL=$VITE_API_URL
RUN npm run build

FROM node:22-alpine
RUN npm install -g serve
WORKDIR /app
COPY --from=build /app/dist ./dist
EXPOSE 80
CMD ["serve", "-s", "dist", "-l", "80"]