import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import * as React from "react";
import { useState } from "react";
import { GridSelectionModel } from "@mui/x-data-grid/models/gridSelectionModel";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { authAtom, modalStateAtom } from "../../../../atoms";
import { fetchDeleteUser } from "../../../../api";
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
  userList: GridSelectionModel;
};

function UserCompDel({ userList }: props) {
  const [loading, setLoading] = useState(false);
  const jwt = useRecoilValue(authAtom);
  const setModalState = useSetRecoilState(modalStateAtom);

  return (
    <Box sx={style}>
      <Typography id="modal-modal-title" variant="h6" component="h2">
        계정 삭제
      </Typography>
      <Typography sx={{ mt: 2 }}>
        삭제할 유저 : {userList.join(" / ")}
      </Typography>
      <br />
      <Box display="flex">
        <LoadingButton
          loading={loading}
          sx={{ mt: 1, ml: "auto" }}
          variant="contained"
          onClick={() => {
            setLoading((prevState) => !prevState);
            fetchDeleteUser(jwt.accessToken, userList)
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
          제거
        </LoadingButton>
      </Box>
    </Box>
  );
}

export default UserCompDel;
