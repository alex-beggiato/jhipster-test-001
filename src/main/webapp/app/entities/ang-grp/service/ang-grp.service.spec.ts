import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAngGrp, AngGrp } from '../ang-grp.model';

import { AngGrpService } from './ang-grp.service';

describe('AngGrp Service', () => {
  let service: AngGrpService;
  let httpMock: HttpTestingController;
  let elemDefault: IAngGrp;
  let expectedResult: IAngGrp | IAngGrp[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AngGrpService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      uid: 'AAAAAAA',
      grpCod: 'AAAAAAA',
      grpDsc: 'AAAAAAA',
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

    it('should create a AngGrp', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AngGrp()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AngGrp', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          grpCod: 'BBBBBB',
          grpDsc: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AngGrp', () => {
      const patchObject = Object.assign(
        {
          uid: 'BBBBBB',
        },
        new AngGrp()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AngGrp', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          grpCod: 'BBBBBB',
          grpDsc: 'BBBBBB',
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

    it('should delete a AngGrp', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAngGrpToCollectionIfMissing', () => {
      it('should add a AngGrp to an empty array', () => {
        const angGrp: IAngGrp = { id: 123 };
        expectedResult = service.addAngGrpToCollectionIfMissing([], angGrp);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(angGrp);
      });

      it('should not add a AngGrp to an array that contains it', () => {
        const angGrp: IAngGrp = { id: 123 };
        const angGrpCollection: IAngGrp[] = [
          {
            ...angGrp,
          },
          { id: 456 },
        ];
        expectedResult = service.addAngGrpToCollectionIfMissing(angGrpCollection, angGrp);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AngGrp to an array that doesn't contain it", () => {
        const angGrp: IAngGrp = { id: 123 };
        const angGrpCollection: IAngGrp[] = [{ id: 456 }];
        expectedResult = service.addAngGrpToCollectionIfMissing(angGrpCollection, angGrp);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(angGrp);
      });

      it('should add only unique AngGrp to an array', () => {
        const angGrpArray: IAngGrp[] = [{ id: 123 }, { id: 456 }, { id: 29446 }];
        const angGrpCollection: IAngGrp[] = [{ id: 123 }];
        expectedResult = service.addAngGrpToCollectionIfMissing(angGrpCollection, ...angGrpArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const angGrp: IAngGrp = { id: 123 };
        const angGrp2: IAngGrp = { id: 456 };
        expectedResult = service.addAngGrpToCollectionIfMissing([], angGrp, angGrp2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(angGrp);
        expect(expectedResult).toContain(angGrp2);
      });

      it('should accept null and undefined values', () => {
        const angGrp: IAngGrp = { id: 123 };
        expectedResult = service.addAngGrpToCollectionIfMissing([], null, angGrp, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(angGrp);
      });

      it('should return initial array if no AngGrp is added', () => {
        const angGrpCollection: IAngGrp[] = [{ id: 123 }];
        expectedResult = service.addAngGrpToCollectionIfMissing(angGrpCollection, undefined, null);
        expect(expectedResult).toEqual(angGrpCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
