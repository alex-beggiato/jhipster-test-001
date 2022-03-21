import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AngResService } from '../service/ang-res.service';
import { IAngRes, AngRes } from '../ang-res.model';
import { IAngBdg } from 'app/entities/ang-bdg/ang-bdg.model';
import { AngBdgService } from 'app/entities/ang-bdg/service/ang-bdg.service';
import { IAngGrp } from 'app/entities/ang-grp/ang-grp.model';
import { AngGrpService } from 'app/entities/ang-grp/service/ang-grp.service';

import { AngResUpdateComponent } from './ang-res-update.component';

describe('AngRes Management Update Component', () => {
  let comp: AngResUpdateComponent;
  let fixture: ComponentFixture<AngResUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let angResService: AngResService;
  let angBdgService: AngBdgService;
  let angGrpService: AngGrpService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AngResUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AngResUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AngResUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    angResService = TestBed.inject(AngResService);
    angBdgService = TestBed.inject(AngBdgService);
    angGrpService = TestBed.inject(AngGrpService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call bdgUid query and add missing value', () => {
      const angRes: IAngRes = { id: 456 };
      const bdgUid: IAngBdg = { id: 86939 };
      angRes.bdgUid = bdgUid;

      const bdgUidCollection: IAngBdg[] = [{ id: 65112 }];
      jest.spyOn(angBdgService, 'query').mockReturnValue(of(new HttpResponse({ body: bdgUidCollection })));
      const expectedCollection: IAngBdg[] = [bdgUid, ...bdgUidCollection];
      jest.spyOn(angBdgService, 'addAngBdgToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ angRes });
      comp.ngOnInit();

      expect(angBdgService.query).toHaveBeenCalled();
      expect(angBdgService.addAngBdgToCollectionIfMissing).toHaveBeenCalledWith(bdgUidCollection, bdgUid);
      expect(comp.bdgUidsCollection).toEqual(expectedCollection);
    });

    it('Should call AngGrp query and add missing value', () => {
      const angRes: IAngRes = { id: 456 };
      const uids: IAngGrp[] = [{ id: 51862 }];
      angRes.uids = uids;

      const angGrpCollection: IAngGrp[] = [{ id: 29691 }];
      jest.spyOn(angGrpService, 'query').mockReturnValue(of(new HttpResponse({ body: angGrpCollection })));
      const additionalAngGrps = [...uids];
      const expectedCollection: IAngGrp[] = [...additionalAngGrps, ...angGrpCollection];
      jest.spyOn(angGrpService, 'addAngGrpToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ angRes });
      comp.ngOnInit();

      expect(angGrpService.query).toHaveBeenCalled();
      expect(angGrpService.addAngGrpToCollectionIfMissing).toHaveBeenCalledWith(angGrpCollection, ...additionalAngGrps);
      expect(comp.angGrpsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const angRes: IAngRes = { id: 456 };
      const bdgUid: IAngBdg = { id: 78140 };
      angRes.bdgUid = bdgUid;
      const uids: IAngGrp = { id: 11445 };
      angRes.uids = [uids];

      activatedRoute.data = of({ angRes });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(angRes));
      expect(comp.bdgUidsCollection).toContain(bdgUid);
      expect(comp.angGrpsSharedCollection).toContain(uids);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AngRes>>();
      const angRes = { id: 123 };
      jest.spyOn(angResService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ angRes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: angRes }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(angResService.update).toHaveBeenCalledWith(angRes);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AngRes>>();
      const angRes = new AngRes();
      jest.spyOn(angResService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ angRes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: angRes }));
      saveSubject.complete();

      // THEN
      expect(angResService.create).toHaveBeenCalledWith(angRes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AngRes>>();
      const angRes = { id: 123 };
      jest.spyOn(angResService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ angRes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(angResService.update).toHaveBeenCalledWith(angRes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAngBdgById', () => {
      it('Should return tracked AngBdg primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAngBdgById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAngGrpById', () => {
      it('Should return tracked AngGrp primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAngGrpById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedAngGrp', () => {
      it('Should return option if no AngGrp is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedAngGrp(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected AngGrp for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedAngGrp(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this AngGrp is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedAngGrp(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
