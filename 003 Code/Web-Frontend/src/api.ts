import { GridSelectionModel } from "@mui/x-data-grid/models/gridSelectionModel";

import { ITransactionRequest, IUserJoinRequest, IUserModifyReq, UserRole } from "./interfaces";
import axios from "axios";

const BASE_URL = "http://119.203.225.3:80";

axios.interceptors.response.use(
  function(response) {
    return response;
  },

  function(error) {
    if (error.response.status === 401 || error.response.status == 403) {
      localStorage.clear();
      alert("잘못된 접근 입니다.");
      window.location.href = "/login";
    } else {
      console.log(error);
    }
    return Promise.reject(error);
  }
);

export const fetchAllUser = (jwt: string, page: number) => {
  return axios({
    method: "GET",
    url: `${BASE_URL}/admin/users?`,
    headers: { "Content-Type": "application/json", jwt },
    params: { page: page }
  });
};

export const fetchCoinUsage = (
  jwt: string,
  coinName: string,
  fromLocalDateTime: string,
  toLocalDateTime: string
) => {
  return axios({
    method: "GET",
    url: `${BASE_URL}/admin/trades/coin`,
    params: {
      coinName: coinName,
      fromLocalDateTime: fromLocalDateTime,
      toLocalDateTime: toLocalDateTime
    },
    headers: { "Content-Type": "application/json", jwt }
  });
};

export const fetchAllCoins = (jwt: string) => {
  return axios({
    method: "GET",
    url: `${BASE_URL}/admin/coins`,
    headers: { "Content-Type": "application/json", jwt }
  });
};

export const fetchCreateCoin = (jwt: string, coinName: string) => {
  return axios({
    method: "POST",
    url: `${BASE_URL}/admin/coin`,
    headers: { "Content-Type": "application/json", jwt },
    data: { coinName: coinName }
  });
};

export const fetchDeleteCoin = (jwt: string, coinNames: GridSelectionModel) => {
  return axios({
    method: "DELETE",
    url: `${BASE_URL}/admin/coin`,
    headers: { "Content-Type": "application/json", jwt },
    data: { coinNameList: coinNames }
  });
};

export const fetchTransferCoinAll = (
  jwt: string,
  coinName: string,
  coinValue: string,
  userRole: UserRole
) => {
  return axios({
    method: "POST",
    url: `${BASE_URL}/admin/coin/update/assets`,
    headers: { "Content-Type": "application/json", jwt },
    data: {
      coinName: coinName,
      coinValue: coinValue,
      userRole: userRole
    }
  });
};

export const fetchTransferCoin = (
  jwt: string,
  coinName: string,
  coinValue: string,
  userNameList: string[]
) => {
  return axios({
    method: "POST",
    url: `${BASE_URL}/admin/coin/update/asset`,
    headers: { "Content-Type": "application/json", jwt },
    data: {
      coinName: coinName,
      coinValue: coinValue,
      identifier: userNameList
    }
  });
};

export const fetchDeleteUser = (
  jwt: string,
  identifier: GridSelectionModel
) => {
  return axios({
    method: "DELETE",
    url: `${BASE_URL}/admin/user`,
    headers: { "Content-Type": "application/json", jwt },
    data: {
      identifier: identifier
    }
  });
};

export const fetchUpdateUser = (jwt: string, userDto: IUserModifyReq) => {
  return axios({
    method: "PUT",
    url: `${BASE_URL}/admin/user`,
    headers: { "Content-Type": "application/json", jwt },
    data: userDto
  });
};

export const fetchCreateStore = (jwt: string, formData: FormData) => {
  return axios({
    method: "POST",
    url: `${BASE_URL}/admin/store`,
    headers: { jwt },
    data: formData
  });
};

export const fetchAllStore = (jwt: string, page: number) => {
  return axios({
    method: "GET",
    url: `${BASE_URL}/user/stores?page=${page}`,
    headers: { "Content-Type": "application/json", jwt }
  });
};

export const fetchDeleteStore = (
  jwt: string,
  name: string,
  phoneNumber: string
) => {
  return axios({
    method: "DELETE",
    url: `${BASE_URL}/admin/store`,
    headers: {
      "Content-Type": "application/json",
      jwt
    },
    data: {
      name: name,
      phoneNumber: phoneNumber
    }
  });
};

export const fetchTransaction = (
  jwt: string,
  transactionRequest: ITransactionRequest
) => {
  return axios({
    method: "GET",
    url: `${BASE_URL}/admin/trade`,
    params: transactionRequest,
    headers: { "Content-Type": "application/json", jwt }
  });
};

export const fetchCreateUser = (
  jwt: string,
  UserJoinRequest: IUserJoinRequest
) => {
  return axios({
    method: "POST",
    url: `${BASE_URL}/user/user`,
    headers: { "Content-Type": "application/json", jwt },
    data: UserJoinRequest
  });
};

export const fetchUserCoins = (jwt: string, identifier: string) => {
  return axios({
    method: "GET",
    url: `${BASE_URL}/admin/user`,
    headers: { "Content-Type": "application/json", jwt },
    params: { identifier: identifier }
  });
};
