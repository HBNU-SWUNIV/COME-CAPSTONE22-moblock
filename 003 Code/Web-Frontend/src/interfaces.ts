export enum UserRole {
  ROLE_STUDENT = "ROLE_STUDENT",
  ROLE_STOREMANAGER = "ROLE_STOREMANAGER",
}

export interface IUserDetail {
  dateCreated: string;
  identifier: string;
  lastUpdated: string;
  name: string;
  userRole: UserRole;
}

export interface IPageDetail {
  totalPage: number;
  totalUserNumber: number;
  userDtoList: IUserDetail[];
}

export interface ICoinDtoList {
  issuance: number;
  name: string;
}

export interface ICoinDetail {
  coinDtoList: ICoinDtoList[];
  totalIssuance: number;
}

export interface IUserModifyReq {
  requestedIdentifier: string;
  wantToChangeIdentifier: string;
  wantToChangeName: string;
  wantToChangePlainPassword: string | null;
  wantToChangeUserRole: UserRole;
}

export interface ICreateStoreRequest {
  address: string;
  phoneNumber: string;
  storeName: string;
}

export interface IShopDetail {
  totalUserNumber: number;
  totalPage: number;
  storeResponseList: IShopList[];
}

export interface IShopList {
  name: string;
  phoneNumber: string;
  address: string;
  storeImageFileName: string;
}

export interface ITransactionList {
  amount: number;
  coinName: string;
  dateCreated: string;
  receiverIdentifier: string;
  receiverName: string;
  senderIdentifier: string;
  senderName: string;
  transactionId: string;
}

export interface ITransactionResponse {
  totalTradeNumber: number;
  totalPage: number;
  transferResponseList: ITransactionList[];
}

export interface IUserJoinRequest {
  identifier: string;
  name: string;
  password: string;
  userRole: UserRole;
}

export interface IUserCoins {
  identifier: string;
  owner: string;
  coin: {};
}

export interface ITransactionRequest {
  page: number;
  dateTimeRange: string;
  fromLocalDateTime: string;
  untilLocalDateTime: string;
  receiverIdentifier: string;
  receiverUserRole: string;
  senderIdentifier: string;
  senderUserRole: string;
}

export interface ICoinShare {
  issuance: number;
  name: string;
}