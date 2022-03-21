import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AngGrpDetailComponent } from './ang-grp-detail.component';

describe('AngGrp Management Detail Component', () => {
  let comp: AngGrpDetailComponent;
  let fixture: ComponentFixture<AngGrpDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AngGrpDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ angGrp: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AngGrpDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AngGrpDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load angGrp on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.angGrp).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
