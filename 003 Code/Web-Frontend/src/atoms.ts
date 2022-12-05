import { atom } from "recoil";

const date = new Date();

const year = date.getFullYear();
const month = ("0" + (date.getMonth() + 1)).slice(-2);
const day = ("0" + date.getDate()).slice(-2);
const hours = ("0" + date.getHours()).slice(-2);
const minutes = ("0" + date.getMinutes()).slice(-2);
const seconds = ("0" + date.getSeconds()).slice(-2);

export const authAtom = atom({
  key: "auth",
  default: { accessToken: "" }
});

export const modalStateAtom = atom({
  key: "modalState",
  default: false
});

export const transactionAtom = atom({
  key: "transaction",
  default: {
    page: 1,
    dateTimeRange: "YEAR",
    fromLocalDateTime: `${
      +year - 1
    }-${month}-${day}T${hours}:${minutes}:${seconds}`,
    untilLocalDateTime: `${+year}-${month}-${day}T${hours}:${minutes}:${seconds}`,
    receiverIdentifier: "",
    receiverUserRole: "",
    senderIdentifier: "",
    senderUserRole: ""
  }
});
