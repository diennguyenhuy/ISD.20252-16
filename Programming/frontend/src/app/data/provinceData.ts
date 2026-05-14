import data from './tinhthanhvietnam_moi.json';

export const provinces = data.map(p => p.name);
export const provinceToWards = Object.fromEntries(
    data.map(p => [p.name, p.wards])
);