import * as React from "react";
import { useState } from "react";
import { DataGrid, GridColDef, GridSelectionModel } from "@mui/x-data-grid";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { useRecoilState, useRecoilValue } from "recoil";
import { authAtom, modalStateAtom } from "../../../atoms";
import { useQuery } from "react-query";
import { fetchAllCoins } from "../../../api";
import { Modal } from "@mui/material";
import CoinCompNew from "./modalComp/CoinCompNew";
import DeleteCoin from "./modalComp/CoinCompDel";
import CoinCompTransfer from "./modalComp/CoinCompTransfer";
import CoinCompDeploy from "./modalComp/CoinCompDeploy";
import { ICoinDetail } from "../../../interfaces";

function CoinComp() {
  const jwt = useRecoilValue(authAtom);
  const [modState, setModState] = useRecoilState(modalStateAtom);
  const [selectionModel, setSelectionModel] = useState<GridSelectionModel>([]);
  const [modalComp, setModalComp] = useState(<Box />);
  const handleOpen = () => {
    setModState(true);
  };
  const handleClose = () => setModState(false);
  const { isLoading, data } = useQuery<ICoinDetail>(["allCoins"], () =>
    fetchAllCoins(jwt.accessToken).then((response) => response.data)
  );

  const columns: GridColDef[] = [
    { field: "name", headerName: "CoinName", width: 130 },
    { field: "issuance", headerName: "issuance", width: 130 }
  ];

  return isLoading ? (
    <span>loading...</span>
  ) : (
    <Box style={{ height: "100%", width: "100%", minHeight: "70vh" }}>
      <DataGrid
        getRowId={(row) => row.name}
        rows={data!.coinDtoList}
        columns={columns}
        hideFooter={true}
        checkboxSelection
        onSelectionModelChange={(newSelectionModel) => {
          setSelectionModel(newSelectionModel);
        }}
        selectionModel={selectionModel}
      />
      <Box display="flex">
        <Button
          onClick={() => {
            setModalComp(<CoinCompNew />);
            handleOpen();
          }}
        >
          <Typography>코인 발행</Typography>
        </Button>
        <Button
          onClick={() => {
            setModalComp(<CoinCompDeploy coinList={data!.coinDtoList} />);
            handleOpen();
          }}
        >
          <Typography>코인 배포</Typography>
        </Button>
        <Button
          onClick={() => {
            setModalComp(<CoinCompTransfer coinList={data!.coinDtoList} />);
            handleOpen();
          }}
        >
          <Typography>코인 전송</Typography>
        </Button>
        <Button
          onClick={() => {
            setModalComp(<DeleteCoin coinNames={selectionModel} />);
            handleOpen();
          }}
        >
          <Typography>코인 제거</Typography>
        </Button>
        <Modal
          open={modState}
          onClose={handleClose}
          aria-labelledby="modal-modal-title"
          aria-describedby="modal-modal-description"
        >
          {modalComp}
        </Modal>
      </Box>
    </Box>
  );
}

export default CoinComp;
