import { Component, OnInit } from '@angular/core';
import { Risque } from 'src/app/models/Risque';
import { RisqueService } from 'src/app/services/apiServices/risqueService/risque.service';

@Component({
  selector: 'app-risque-list',
  templateUrl: './risque-list.component.html',
  styleUrls: ['./risque-list.component.css']
})
export class RisqueListComponent implements OnInit {
  risques: Risque[] = [];

  constructor(private risqueService: RisqueService) {}

  ngOnInit(): void {
    this.risqueService.getAllRisques().subscribe({
      next: (data) => {
        console.log('Risks fetched from service:', data);
        this.risques = data;
      },
      error: (err) => console.error('Error fetching risques:', err)
    });
  }
}
