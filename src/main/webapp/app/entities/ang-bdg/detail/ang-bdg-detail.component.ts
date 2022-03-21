import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAngBdg } from '../ang-bdg.model';

@Component({
  selector: 'jhi-ang-bdg-detail',
  templateUrl: './ang-bdg-detail.component.html',
})
export class AngBdgDetailComponent implements OnInit {
  angBdg: IAngBdg | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ angBdg }) => {
      this.angBdg = angBdg;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
