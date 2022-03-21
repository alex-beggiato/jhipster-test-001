import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAngBdg, AngBdg } from '../ang-bdg.model';

import { AngBdgService } from './ang-bdg.service';

describe('AngBdg Service', () => {
  let service: AngBdgService;
  let httpMock: HttpTestingController;
  let elemDefault: IAngBdg;
  let expectedResult: IAngBdg | IAngBdg[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AngBdgService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      uid: 'AAAAAAA',
      bdgCod: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AngBdg', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AngBdg()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AngBdg', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          bdgCod: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AngBdg', () => {
      const patchObject = Object.assign({}, new AngBdg());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AngBdg', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          bdgCod: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AngBdg', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAngBdgToCollectionIfMissing', () => {
      it('should add a AngBdg to an empty array', () => {
        const angBdg: IAngBdg = { id: 123 };
        expectedResult = service.addAngBdgToCollectionIfMissing([], angBdg);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(angBdg);
      });

      it('should not add a AngBdg to an array that contains it', () => {
        const angBdg: IAngBdg = { id: 123 };
        const angBdgCollection: IAngBdg[] = [
          {
            ...angBdg,
          },
          { id: 456 },
        ];
        expectedResult = service.addAngBdgToCollectionIfMissing(angBdgCollection, angBdg);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AngBdg to an array that doesn't contain it", () => {
        const angBdg: IAngBdg = { id: 123 };
        const angBdgCollection: IAngBdg[] = [{ id: 456 }];
        expectedResult = service.addAngBdgToCollectionIfMissing(angBdgCollection, angBdg);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(angBdg);
      });

      it('should add only unique AngBdg to an array', () => {
        const angBdgArray: IAngBdg[] = [{ id: 123 }, { id: 456 }, { id: 34043 }];
        const angBdgCollection: IAngBdg[] = [{ id: 123 }];
        expectedResult = service.addAngBdgToCollectionIfMissing(angBdgCollection, ...angBdgArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const angBdg: IAngBdg = { id: 123 };
        const angBdg2: IAngBdg = { id: 456 };
        expectedResult = service.addAngBdgToCollectionIfMissing([], angBdg, angBdg2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(angBdg);
        expect(expectedResult).toContain(angBdg2);
      });

      it('should accept null and undefined values', () => {
        const angBdg: IAngBdg = { id: 123 };
        expectedResult = service.addAngBdgToCollectionIfMissing([], null, angBdg, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(angBdg);
      });

      it('should return initial array if no AngBdg is added', () => {
        const angBdgCollection: IAngBdg[] = [{ id: 123 }];
        expectedResult = service.addAngBdgToCollectionIfMissing(angBdgCollection, undefined, null);
        expect(expectedResult).toEqual(angBdgCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
