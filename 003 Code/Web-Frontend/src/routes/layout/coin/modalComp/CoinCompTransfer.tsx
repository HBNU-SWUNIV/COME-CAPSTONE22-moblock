import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import { InputLabel, NativeSelect } from "@mui/material";
import { ICoinDtoList } from "../../../../interfaces";
import * as React from "react";
import { useState } from "react";
import Button from "@mui/material/Button";
import { fetchTransferCoin } from "../../../../api";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { authAtom, modalStateAtom } from "../../../../atoms";
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

function CoinCompTransfer({ coinList }: props) {
  const [loading, setLoading] = useState(false);
  const [user, setUser] = useState("");
  const [coinName, setCoinName] = useState(coinList[0].name);
  const [coinValue, setCoinValue] = useState("");
  const [userList, setUserList] = useState<string[]>([]);
  const jwt = useRecoilValue(authAtom);
  const setModalState = useSetRecoilState(modalStateAtom);

  const coinOptions = coinList.map((value) => <option>{value.name}</option>);

  return (
    <Box sx={style}>
      <Typography id="modal-modal-title" variant="h6" component="h2">
        코인 전송
      </Typography>
      <Grid container spacing={1} sx={{ mt: 2 }}>
        <Grid item xs={12}>
          <InputLabel variant="standard" htmlFor="coin-native">
            User
          </InputLabel>
          <TextField
            size="small"
            sx={{ mt: 1 }}
            label="학번"
            variant="outlined"
            onChange={(event) => setUser(event.target.value)}
          />
          <Button
            sx={{ ml: 2, mt: 1 }}
            variant="outlined"
            onClick={() => setUserList([...userList, user])}
          >
            추가
          </Button>
        </Grid>
        <Grid item xs={12}>
          <InputLabel variant="standard" htmlFor="coin-native">
            Coin
          </InputLabel>
          <Box sx={{ display: "flex" }}>
            <NativeSelect
              inputProps={{
                name: "Coin",
                id: "coin-native"
              }}
              onChange={(event) => setCoinName(event.target.value)}
            >
              {coinOptions}
            </NativeSelect>
            <TextField
              sx={{ ml: 3 }}
              label="코인수량"
              variant="outlined"
              size="small"
              onChange={(event) => setCoinValue(event.target.value)}
            />
          </Box>
        </Grid>
      </Grid>
      <br />
      <Typography>선택된 학생 : {userList}</Typography>
      <br />
      <Box display="flex">
        <LoadingButton
          loading={loading}
          sx={{ ml: "auto", mt: 1 }}
          variant="contained"
          onClick={() => {
            setLoading((prevState) => !prevState);
            fetchTransferCoin(jwt.accessToken, coinName, coinValue, userList)
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
          전송
        </LoadingButton>
      </Box>
    </Box>
  );
}

export default CoinCompTransfer;
