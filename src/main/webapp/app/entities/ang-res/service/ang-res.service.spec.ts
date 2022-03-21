import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AngResTyp } from 'app/entities/enumerations/ang-res-typ.model';
import { IAngRes, AngRes } from '../ang-res.model';

import { AngResService } from './ang-res.service';

describe('AngRes Service', () => {
  let service: AngResService;
  let httpMock: HttpTestingController;
  let elemDefault: IAngRes;
  let expectedResult: IAngRes | IAngRes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AngResService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      uid: 'AAAAAAA',
      resCod: 'AAAAAAA',
      resDsc: 'AAAAAAA',
      bdgUid: 0,
      resTyp: AngResTyp.H,
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

    it('should create a AngRes', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AngRes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AngRes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          resCod: 'BBBBBB',
          resDsc: 'BBBBBB',
          bdgUid: 1,
          resTyp: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AngRes', () => {
      const patchObject = Object.assign(
        {
          resCod: 'BBBBBB',
          resDsc: 'BBBBBB',
        },
        new AngRes()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AngRes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uid: 'BBBBBB',
          resCod: 'BBBBBB',
          resDsc: 'BBBBBB',
          bdgUid: 1,
          resTyp: 'BBBBBB',
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

    it('should delete a AngRes', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAngResToCollectionIfMissing', () => {
      it('should add a AngRes to an empty array', () => {
        const angRes: IAngRes = { id: 123 };
        expectedResult = service.addAngResToCollectionIfMissing([], angRes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(angRes);
      });

      it('should not add a AngRes to an array that contains it', () => {
        const angRes: IAngRes = { id: 123 };
        const angResCollection: IAngRes[] = [
          {
            ...angRes,
          },
          { id: 456 },
        ];
        expectedResult = service.addAngResToCollectionIfMissing(angResCollection, angRes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AngRes to an array that doesn't contain it", () => {
        const angRes: IAngRes = { id: 123 };
        const angResCollection: IAngRes[] = [{ id: 456 }];
        expectedResult = service.addAngResToCollectionIfMissing(angResCollection, angRes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(angRes);
      });

      it('should add only unique AngRes to an array', () => {
        const angResArray: IAngRes[] = [{ id: 123 }, { id: 456 }, { id: 59665 }];
        const angResCollection: IAngRes[] = [{ id: 123 }];
        expectedResult = service.addAngResToCollectionIfMissing(angResCollection, ...angResArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const angRes: IAngRes = { id: 123 };
        const angRes2: IAngRes = { id: 456 };
        expectedResult = service.addAngResToCollectionIfMissing([], angRes, angRes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(angRes);
        expect(expectedResult).toContain(angRes2);
      });

      it('should accept null and undefined values', () => {
        const angRes: IAngRes = { id: 123 };
        expectedResult = service.addAngResToCollectionIfMissing([], null, angRes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(angRes);
      });

      it('should return initial array if no AngRes is added', () => {
        const angResCollection: IAngRes[] = [{ id: 123 }];
        expectedResult = service.addAngResToCollectionIfMissing(angResCollection, undefined, null);
        expect(expectedResult).toEqual(angResCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
