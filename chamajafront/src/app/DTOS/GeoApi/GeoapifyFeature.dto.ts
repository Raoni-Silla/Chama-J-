import { GeoapifyProperties } from "./GeoapifyProperties.dto";

export interface GeoapifyFeature {
    type : string;
    properties : GeoapifyProperties;
}