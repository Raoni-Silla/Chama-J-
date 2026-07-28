export interface EnderecoRequestDTO {
    logradouro: string;
    numero: number;
    complemento?: string; 
    nomeCidade: string;
    siglaEstado: string;
    cep: string;
    latitude: number;
    longitude: number;
    enderecoPrincipal: boolean;
}