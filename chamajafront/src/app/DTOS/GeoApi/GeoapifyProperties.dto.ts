export interface GeoapifyProperties {
    state_code: string;
    country: string;
    country_code: string;
    state: string;
    city?: string;
    postcode?: string;
    street?: string;
    housenumber?: string;
    formatted: string; 
    lat: number;
    lon: number;
}