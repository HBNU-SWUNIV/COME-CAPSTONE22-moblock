import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import * as React from "react";
import { useState } from "react";
import { fetchDeleteStore } from "../../../../api";
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
  name: string;
  phoneNumber: string;
};

function ShopCompDel({ name, phoneNumber }: props) {
  const [loading, setLoading] = useState(false);

  const jwt = useRecoilValue(authAtom);
  const setModalState = useSetRecoilState(modalStateAtom);

  return (
    <Box sx={style}>
      <Typography id="modal-modal-title" variant="h6" component="h2">
        가맹점 삭제
      </Typography>
      <Typography id="modal-modal-description" sx={{ mt: 2 }}>
        삭제할 가맹점 : {name} _ {phoneNumber}
      </Typography>
      <br />
      <Box display="flex">
        <LoadingButton
          loading={loading}
          sx={{ ml: "auto", mt: 1 }}
          variant="contained"
          onClick={() => {
            setLoading((prevState) => !prevState);
            fetchDeleteStore(jwt.accessToken, name, phoneNumber)
              .then((response) => {
                setLoading((prevState) => !prevState);
                setModalState((prevState) => !prevState);
                alert("삭제 성공");
              })
              .catch((e) => {
                alert(e.response.data.message);
                setModalState((prevState) => !prevState);
              });
          }}
        >
          삭제하기
        </LoadingButton>
      </Box>
    </Box>
  );
}

export default ShopCompDel;
