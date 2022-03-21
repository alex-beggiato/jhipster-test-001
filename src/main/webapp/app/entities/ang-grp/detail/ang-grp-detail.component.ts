import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAngGrp } from '../ang-grp.model';

@Component({
  selector: 'jhi-ang-grp-detail',
  templateUrl: './ang-grp-detail.component.html',
})
export class AngGrpDetailComponent implements OnInit {
  angGrp: IAngGrp | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ angGrp }) => {
      this.angGrp = angGrp;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
