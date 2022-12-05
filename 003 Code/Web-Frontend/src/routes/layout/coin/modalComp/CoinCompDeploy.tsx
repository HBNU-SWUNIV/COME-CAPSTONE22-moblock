import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import { InputLabel, NativeSelect } from "@mui/material";
import Grid from "@mui/material/Grid";
import * as React from "react";
import { useState } from "react";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { authAtom, modalStateAtom } from "../../../../atoms";
import { ICoinDtoList, UserRole } from "../../../../interfaces";
import { fetchTransferCoinAll } from "../../../../api";
import LoadingButton from "@mui/lab/LoadingButton";

const style = {
  position: "absolute" as "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  p: 4
};

type props = {
  coinList: ICoinDtoList[];
};

function CoinCompDeploy({ coinList }: props) {
  const [loading, setLoading] = useState(false);

  const [coinValue, setCoinValue] = useState("");
  const [coinName, setCoinName] = useState(coinList[0].name);
  const [userRole, setUserRole] = useState(UserRole.ROLE_STUDENT);

  const jwt = useRecoilValue(authAtom);
  const setModalState = useSetRecoilState(modalStateAtom);

  const coinOptions = coinList.map((value) => <option>{value.name}</option>);

  return (
    <Box sx={style}>
      <Typography id="modal-modal-title" variant="h6" component="h2">
        코인 배포
      </Typography>
      <Grid container spacing={2} sx={{ mt: 2 }}>
        <Grid item xs={4}>
          <InputLabel variant="standard" htmlFor="coin-native">
            Coin
          </InputLabel>
          <NativeSelect
            onChange={(event) => setCoinName(event.target.value)}
            inputProps={{
              name: "Coin",
              id: "coin-native"
            }}
          >
            {coinOptions}
          </NativeSelect>
        </Grid>
        <Grid item xs={4}>
          <InputLabel variant="standard" htmlFor="coin-native">
            Role
          </InputLabel>
          <NativeSelect
            onChange={(event) =>
              setUserRole(
                event.target.value == "1"
                  ? UserRole.ROLE_STUDENT
                  : UserRole.ROLE_STOREMANAGER
              )
            }
            inputProps={{
              name: "Role",
              id: "role-native"
            }}
          >
            <option value={1}>학생</option>
            <option value={2}>상점</option>
          </NativeSelect>
        </Grid>
        <Grid item xs={4}>
          <TextField
            label="코인수량"
            variant="outlined"
            size="small"
            sx={{ mt: 2 }}
            onChange={(event) => setCoinValue(event.target.value)}
          />
        </Grid>
      </Grid>
      <br />
      <Box display="flex">
        <LoadingButton
          loading={loading}
          sx={{ ml: "auto", mt: 1 }}
          variant="contained"
          onClick={() => {
            setLoading((prevState) => !prevState);
            fetchTransferCoinAll(jwt.accessToken, coinName, coinValue, userRole)
              .then((response) => {
                setLoading((prevState) => !prevState);
                setModalState((prevState) => !prevState);
                alert("전송 송공");
              })
              .catch((e) => {
                alert(e.response.data.message);
                setModalState((prevState) => !prevState);
              });
          }}
        >
          배포
        </LoadingButton>
      </Box>
    </Box>
  );
}

export default CoinCompDeploy;
