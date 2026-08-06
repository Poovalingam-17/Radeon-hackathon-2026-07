/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        guardian: {
          50: '#f5f8ff',
          100: '#ebf1ff',
          200: '#d6e4ff',
          300: '#b0ccff',
          400: '#80adff',
          500: '#4d87ff',
          600: '#2b5eff',
          700: '#1a47eb',
          800: '#143abc',
          900: '#163594',
          950: '#0f1f58',
        },
        slate: {
          950: '#070a13',
        }
      },
    },
  },
  plugins: [],
}
