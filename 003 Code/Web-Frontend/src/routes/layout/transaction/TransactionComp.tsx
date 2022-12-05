import * as React from "react";
import { useEffect, useState } from "react";
import { DataGrid, GridCellParams, GridColDef } from "@mui/x-data-grid";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { useRecoilState, useRecoilValue, useResetRecoilState } from "recoil";
import { authAtom, modalStateAtom, transactionAtom } from "../../../atoms";
import { useQuery } from "react-query";
import { fetchTransaction } from "../../../api";
import { IconButton, Modal } from "@mui/material";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import { ITransactionList, ITransactionResponse } from "../../../interfaces";
import Grid from "@mui/material/Grid";
import TransactionCompSender from "./modalComp/TransactionCompSender";
import TransactionCompReceiver from "./modalComp/TransactionCompReceiver";
import TransactionCompDate from "./modalComp/TransactionCompDate";

function TransactionComp() {
  const jwt = useRecoilValue(authAtom);
  const [modState, setModState] = useRecoilState(modalStateAtom);
  const [transactionRequest, setTransactionRequest] =
    useRecoilState(transactionAtom);
  const transactionRequestReset = useResetRecoilState(transactionAtom);
  const [transactionDetail, setTransactionDetail] =
    useState<ITransactionList>();
  const [modalComp, setModalComp] = useState(<Box />);
  const [detailTable, setDetailTable] = useState(false);

  const handleOpen = () => {
    setModState(true);
  };
  const handleClose = () => setModState(false);

  useEffect(() => transactionRequestReset, []);

  const { isLoading, data } = useQuery<ITransactionResponse>(
    ["allTransaction", transactionRequest],
    async () =>
      await fetchTransaction(jwt.accessToken, transactionRequest).then(
        (response) => response.data
      )
  );

  const columns: GridColDef[] = [
    { field: "senderIdentifier", headerName: "Sender", width: 200 },
    { field: "receiverIdentifier", headerName: "Receiver", width: 200 },
    { field: "dateCreated", headerName: "Date", width: 200 },
    { field: "coinName", headerName: "Coin", width: 100 },
    { field: "amount", headerName: "Amount", width: 100 }
  ];

  return isLoading ? (
    <span>loading...</span>
  ) : (
    <Box style={{ height: "100%", width: "100%", minHeight: "50vh" }}>
      <DataGrid
        getRowId={(row) => row.transactionId}
        rows={data!.transferResponseList}
        columns={columns}
        hideFooter={true}
        onCellClick={(params: GridCellParams) => {
          setTransactionDetail(params.row);
          setDetailTable(true);
        }}
      />
      <Box display="flex">
        <Button
          onClick={() => {
            setModalComp(<TransactionCompDate />);
            handleOpen();
          }}
        >
          <Typography>날짜 검색</Typography>
        </Button>
        <Button
          onClick={() => {
            setModalComp(<TransactionCompSender />);
            handleOpen();
          }}
        >
          <Typography>송신자 검색</Typography>
        </Button>
        <Button
          onClick={() => {
            setModalComp(<TransactionCompReceiver />);
            handleOpen();
          }}
        >
          <Typography>수신자 검색</Typography>
        </Button>
        <IconButton
          aria-label="backward"
          disabled={transactionRequest.page < 2}
          sx={{ ml: "auto" }}
          onClick={() =>
            setTransactionRequest((currVal) => {
              const page = transactionRequest.page - 1;
              return { ...currVal, page };
            })
          }
        >
          <ArrowBackIosNewIcon />
        </IconButton>
        <IconButton
          aria-label="forward"
          disabled={
            transactionRequest.page == data!.totalPage || data!.totalPage == 0
          }
          onClick={() => {
            setTransactionRequest((currVal) => {
              const page = transactionRequest.page + 1;
              return { ...currVal, page };
            });
          }}
        >
          <ArrowForwardIosIcon />
        </IconButton>
        <Modal
          open={modState}
          onClose={handleClose}
          aria-labelledby="modal-modal-title"
          aria-describedby="modal-modal-description"
        >
          {modalComp}
        </Modal>
      </Box>
      <br />
      {detailTable ? (
        <Grid container sx={{ border: "1px solid #E0E0E0", borderRadius: 1 }}>
          <Grid item xs={6}>
            <Typography sx={{ ml: 2, mt: 1 }}>
              transactionId : {transactionDetail?.transactionId}
            </Typography>
            <Typography sx={{ ml: 2, mt: 1 }}>
              coinName : {transactionDetail?.coinName}
            </Typography>
            <Typography sx={{ ml: 2, mt: 1 }}>
              amount : {transactionDetail?.amount}
            </Typography>
            <Typography sx={{ ml: 2, mt: 1, mb: 1 }}>
              dateCreated : {transactionDetail?.dateCreated}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography sx={{ ml: 2, mt: 1 }}>
              senderIdentifier : {transactionDetail?.senderIdentifier}
            </Typography>
            <Typography sx={{ ml: 2, mt: 1 }}>
              senderName : {transactionDetail?.senderName}
            </Typography>
            <Typography sx={{ ml: 2, mt: 1 }}>
              receiverIdentifier : {transactionDetail?.receiverIdentifier}
            </Typography>
            <Typography sx={{ ml: 2, mt: 1, mb: 1 }}>
              receiverName : {transactionDetail?.receiverName}
            </Typography>
          </Grid>
        </Grid>
      ) : (
        <Box />
      )}
    </Box>
  );
}

export default TransactionComp;
