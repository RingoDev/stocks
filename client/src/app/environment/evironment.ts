let url:string

if (!process.env.NODE_ENV || process.env.NODE_ENV === 'development') {
    url = 'http://localhost:8080/api'
} else {
    url = 'https://backend.ringodev.com:8443/api'
}

export const BACKEND_URL = url