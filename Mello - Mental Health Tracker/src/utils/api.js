// Central API base URL.
// In local dev (Vite proxy): empty string — /api/* is proxied to localhost:8080
// On Netlify (production): set VITE_API_BASE_URL=https://your-backend.up.railway.app
//   in Netlify → Site Settings → Environment Variables, then re-deploy.
const API_BASE = import.meta.env.VITE_API_BASE_URL ?? '';
export default API_BASE;
