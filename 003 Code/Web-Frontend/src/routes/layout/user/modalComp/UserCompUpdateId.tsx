import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import * as React from "react";
import { useEffect, useState } from "react";
import { IUserDetail, IUserModifyReq, UserRole } from "../../../../interfaces";
import TextField from "@mui/material/TextField";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { authAtom, modalStateAtom } from "../../../../atoms";
import { fetchUpdateUser } from "../../../../api";
import { InputLabel } from "@mui/material";
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
  userDto: IUserDetail;
};

function UserCompUpdateId({ userDto }: props) {
  const [loading, setLoading] = useState(false);

  const jwt = useRecoilValue(authAtom);
  const setModalState = useSetRecoilState(modalStateAtom);
  const [userId, setUserID] = useState("");
  const [reqDto, setReqDto] = useState<IUserModifyReq>({
    wantToChangeUserRole: UserRole.ROLE_STUDENT,
    wantToChangeName: "",
    wantToChangeIdentifier: "",
    wantToChangePlainPassword: null,
    requestedIdentifier: ""
  });

  useEffect(
    () =>
      setReqDto({
        requestedIdentifier: userDto.identifier,
        wantToChangeIdentifier: userId,
        wantToChangeUserRole: userDto.userRole,
        wantToChangeName: userDto.name,
        wantToChangePlainPassword: null
      }),
    [userId]
  );

  return (
    <Box sx={style}>
      <Typography id="modal-modal-title" variant="h6" component="h2">
        ID 변경
      </Typography>
      <Typography sx={{ mt: 2 }}>
        변경할 유저 ID : {userDto.identifier}
      </Typography>
      <br />
      <InputLabel variant="standard" htmlFor="coin-native">
        변경할 ID
      </InputLabel>
      <TextField
        size="small"
        sx={{ mt: 1 }}
        label="ID"
        variant="outlined"
        onChange={(event) => setUserID(event.target.value)}
      />
      <br />
      <Box display="flex">
        <LoadingButton
          loading={loading}
          sx={{ mt: 1, ml: "auto" }}
          variant="contained"
          onClick={() => {
            setLoading((prevState) => !prevState);
            fetchUpdateUser(jwt.accessToken, reqDto)
              .then((response) => {
                setLoading((prevState) => !prevState);
                setModalState((prevState) => !prevState);
                alert("변경 성공");
              })
              .catch((e) => {
                alert(e.response.data.message);
                setModalState((prevState) => !prevState);
              });
          }}
        >
          ID 변경
        </LoadingButton>
      </Box>
    </Box>
  );
}

export default UserCompUpdateId;
