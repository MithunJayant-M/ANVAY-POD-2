export const environment = {
  production: true,
  // Empty in prod for single-service deploy (Angular embedded in Spring Boot
  // via Dockerfile — same origin, no prefix needed). For a split deploy,
  // set this to the backend URL e.g. 'https://anvay-pod-2.onrender.com'.
  apiBaseUrl: 'https://anvay-pod-2.onrender.com'
};
