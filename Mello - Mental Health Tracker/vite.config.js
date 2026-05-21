import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const backendUrl = env.VITE_API_BASE_URL || 'http://localhost:8080'

  return {
    plugins: [react()],
    server: {
      // Expose on all network interfaces so other PCs on the same network can connect
      host: true,
      port: 5173,
      proxy: {
        // Forward all /api calls to the Spring Boot backend
        '/api': {
          target: backendUrl,
          changeOrigin: true,
          secure: false,
        },
      },
    },
    preview: {
      host: true,
      port: 4173,
      proxy: {
        '/api': {
          target: backendUrl,
          changeOrigin: true,
          secure: false,
        },
      },
    },
  }
})
