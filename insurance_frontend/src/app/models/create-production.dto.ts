export interface CreateProductionDTO {
    idProduction: number;
    numeroContrat: string;
    nature: string;
    dateEffet: Date;
    dateEcheance: Date;
    mois: number;
    dureeContrat: number;
    modePayement: string;
    nombreCheque: number;
    numeroCheque: string;
    dateDuCheque: Date;
    primeNette: number;
    prime: number;
    commission: number;
    remarques: string;
  }
  