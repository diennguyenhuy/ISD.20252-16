import axios from "axios";

export const apiClient = axios.create({
    baseURL: "http://localhost:8080",

    withCredentials: true,

    headers: {
        "Content-Type": "application/json",
    },
});

apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

apiClient.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            console.warn("Session expired or unauthorized. Forcing logout...");

            localStorage.removeItem('token');
            localStorage.removeItem('user');

            window.location.href = '/login?expired=true';
        }

        return Promise.reject(error);
    }
);