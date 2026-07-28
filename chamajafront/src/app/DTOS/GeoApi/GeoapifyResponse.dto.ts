import { GeoapifyFeature } from "./GeoapifyFeature.dto";

export interface GeoapifyResponse {
    type : string,
    features : GeoapifyFeature[];
}