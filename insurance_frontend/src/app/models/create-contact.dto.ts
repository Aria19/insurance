import { CreateBanqueDTO } from "./create-banque.dto";
import { CreateProductionDTO } from "./create-production.dto";

export interface CreateContactDTO {
  assure: string;
  societe: string;
  telephone: string;
  email: string;
  msh: string;
  motDePasse: string;
  cin: string;
  carteSejour: string;
  passeport: string;
  matriculeFiscale: string;
  contracts: CreateProductionDTO[];
  transactions: CreateBanqueDTO[];
}
