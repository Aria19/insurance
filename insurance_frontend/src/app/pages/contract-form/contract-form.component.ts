import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ContractService } from 'src/app/services/apiServices/contractService/contract.service';

@Component({
  selector: 'app-contract-form',
  templateUrl: './contract-form.component.html',
  styleUrls: ['./contract-form.component.css']
})
export class ContractFormComponent implements OnInit {
  constructor(
    private contractService: ContractService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  goBack(): void {
    this.location.back();
  }
}
