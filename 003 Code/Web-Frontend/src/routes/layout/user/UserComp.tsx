import * as React from "react";
import { useState } from "react";
import { DataGrid, GridCellParams, GridColDef, GridSelectionModel } from "@mui/x-data-grid";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { useRecoilState, useRecoilValue } from "recoil";
import { authAtom, modalStateAtom } from "../../../atoms";
import { useQuery } from "react-query";
import { fetchAllUser, fetchUserCoins } from "../../../api";
import { IPageDetail, IUserCoins } from "../../../interfaces";
import { IconButton, Modal } from "@mui/material";
import UserCompUpdatePw from "./modalComp/UserCompUpdatePw";
import UserCompNew from "./modalComp/UserCompNew";
import UserCompDel from "./modalComp/UserCompDel";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import UserCompUpdateId from "./modalComp/UserCompUpdateId";
import Grid from "@mui/material/Grid";
import { Skeleton } from "@mui/lab";

function UserComp() {
  const jwt = useRecoilValue(authAtom);
  const [modState, setModState] = useRecoilState(modalStateAtom);
  const [selectionModel, setSelectionModel] = useState<GridSelectionModel>([]);
  const [page, setPage] = useState(1);
  const [modalComp, setModalComp] = useState(<UserCompNew />);
  const [identifier, setIdentifier] = useState("initial");
  const [detailTable, setDetailTable] = useState(false);

  const handleOpen = (flag: number) => {
    switch (flag) {
      case 1:
        setModState(true);
        break;
      default:
        alert("한명의 유저를 선택해 주세요");
        break;
    }
  };
  const handleClose = () => setModState(false);

  const { isLoading: userDataIsLoading, data: userData } =
    useQuery<IPageDetail>(["allUser", page], () =>
      fetchAllUser(jwt.accessToken, page).then((response) => response.data)
    );

  const { isLoading: userCoinsIsLoading, data: userCoins } =
    useQuery<IUserCoins>(["userCoins", identifier], async () => {
      if (identifier === "initial") {
        await setIdentifier(userData!.userDtoList[0].identifier);
      }
      return await fetchUserCoins(jwt.accessToken, identifier).then(
        (response) => response.data
      );
    });

  const columns: GridColDef[] = [
    { field: "identifier", headerName: "ID", width: 130 },
    { field: "name", headerName: "Name", width: 130 },
    { field: "dateCreated", headerName: "Create", width: 200 },
    { field: "lastUpdated", headerName: "Update", width: 200 },
    { field: "userRole", headerName: "Role", width: 200 }
  ];

  return userDataIsLoading ? (
    <span>loading...</span>
  ) : (
    <Box style={{ height: "100%", width: "100%", minHeight: "50vh" }}>
      <DataGrid
        getRowId={(row) => row.identifier}
        rows={userData!.userDtoList}
        columns={columns}
        hideFooter={true}
        checkboxSelection
        onSelectionModelChange={(newSelectionModel) => {
          setSelectionModel(newSelectionModel);
        }}
        selectionModel={selectionModel}
        onCellClick={(params: GridCellParams) => {
          setIdentifier(params.row.identifier);
          setDetailTable(true);
        }}
      />

      <Box display="flex">
        <Button
          onClick={() => {
            setModalComp(<UserCompNew />);
            handleOpen(1);
          }}
        >
          <Typography>계정 생성</Typography>
        </Button>
        <Button
          onClick={() => {
            setModalComp(<UserCompDel userList={selectionModel} />);
            handleOpen(1);
          }}
        >
          <Typography>계정 삭제</Typography>
        </Button>
        <Button
          onClick={() => {
            setModalComp(
              <UserCompUpdateId
                userDto={
                  userData!.userDtoList.filter(
                    (value) => value.identifier === selectionModel[0]
                  )[0]
                }
              />
            );
            handleOpen(selectionModel.length);
          }}
        >
          <Typography>ID 변경</Typography>
        </Button>
        <Button
          onClick={() => {
            setModalComp(
              <UserCompUpdatePw
                userDto={
                  userData!.userDtoList.filter(
                    (value) => value.identifier === selectionModel[0]
                  )[0]
                }
              />
            );
            handleOpen(selectionModel.length);
          }}
        >
          <Typography>PW 변경</Typography>
        </Button>

        <IconButton
          aria-label="backward"
          disabled={page < 2}
          sx={{ ml: "auto" }}
          onClick={() => setPage(page - 1)}
        >
          <ArrowBackIosNewIcon />
        </IconButton>
        <IconButton
          aria-label="forward"
          disabled={page == userData!.totalPage || userData!.totalPage == 0}
          onClick={() => {
            setPage(page + 1);
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
        userCoinsIsLoading ? (
          <Skeleton animation="wave" variant="rounded" height="20vh" />
        ) : (
          <Grid
            container
            sx={{ border: "1px solid #E0E0E0", borderRadius: 1, mb: 4 }}
          >
            <Grid item xs={4}>
              <Typography variant="h6" sx={{ ml: 2 }}>
                {identifier}의 코인
              </Typography>
            </Grid>
            <Grid item xs={8}>
              <Grid container>
                {Object.entries(userCoins!.coin).map(([key, value]) => (
                  <Grid item xs={3}>
                    <Typography sx={{ ml: 2, mt: 1 }}>
                      {key}: {value}
                    </Typography>
                  </Grid>
                ))}
              </Grid>
            </Grid>
          </Grid>
        )
      ) : (
        <Box />
      )}
    </Box>
  );
}

export default UserComp;
