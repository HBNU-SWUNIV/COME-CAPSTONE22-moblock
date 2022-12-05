import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import * as React from "react";
import { useState } from "react";
import { fetchCreateCoin } from "../../../../api";
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

function CoinCompNew() {
  const [coinName, setCoinName] = useState("");
  const [loading, setLoading] = useState(false);
  const jwt = useRecoilValue(authAtom);
  const setModalState = useSetRecoilState(modalStateAtom);

  return (
    <Box sx={style}>
      <Typography id="modal-modal-title" variant="h6" component="h2">
        코인 발행
      </Typography>

      <TextField
        sx={{ mt: 2 }}
        id="outlined-basic"
        label="생성할 코인의 이름"
        variant="outlined"
        size="small"
        onChange={(event) => setCoinName(event.target.value)}
      />
      <br />
      <Box display="flex">
        <LoadingButton
          loading={loading}
          sx={{ mt: 1, ml: "auto" }}
          variant="contained"
          onClick={() => {
            setLoading((prevState) => !prevState);
            fetchCreateCoin(jwt.accessToken, coinName)
              .then((response) => {
                setLoading((prevState) => !prevState);
                setModalState((prevState) => !prevState);
                alert("발행 성공");
              })
              .catch((e) => {
                alert(e.response.data.message);
                setModalState((prevState) => !prevState);
              });
          }}
        >
          발행
        </LoadingButton>
      </Box>
    </Box>
  );
}

export default CoinCompNew;
