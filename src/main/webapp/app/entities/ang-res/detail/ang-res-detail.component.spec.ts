import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AngResDetailComponent } from './ang-res-detail.component';

describe('AngRes Management Detail Component', () => {
  let comp: AngResDetailComponent;
  let fixture: ComponentFixture<AngResDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AngResDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ angRes: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AngResDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AngResDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load angRes on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.angRes).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
