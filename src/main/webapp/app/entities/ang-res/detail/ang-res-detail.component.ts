import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAngRes } from '../ang-res.model';

@Component({
  selector: 'jhi-ang-res-detail',
  templateUrl: './ang-res-detail.component.html',
})
export class AngResDetailComponent implements OnInit {
  angRes: IAngRes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ angRes }) => {
      this.angRes = angRes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
