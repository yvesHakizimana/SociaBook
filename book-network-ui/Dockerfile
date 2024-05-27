# Stage 1: Build the application
FROM node:latest as build-stage
# Set the working directory
WORKDIR /app
# Copy the package.json and package-lock.json files
COPY package*.json ./
# Run a clean install of the dependencies
RUN npm ci
# Install Angular CLI globally
RUN npm install -g @angular/cli
# Copy all the files
COPY . .
# Build The application
RUN npm run build --prod

# Stage 2: Serve the application with nginx
FROM nginx:latest

# Remove the default nginx configuration
RUN rm /etc/nginx/conf.d/default.conf

# Copy the custom nginx configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Copy the build output from the previous stage
COPY --from=build-stage /app/dist/book-network-ui/browser /usr/share/nginx/html

EXPOSE 80

