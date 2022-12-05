import { useRecoilValue, useSetRecoilState } from "recoil";
import { authAtom, modalStateAtom } from "../../../../atoms";
import * as React from "react";
import { ChangeEvent, useRef, useState } from "react";
import { ICreateStoreRequest } from "../../../../interfaces";
import Button from "@mui/material/Button";
import { fetchCreateStore } from "../../../../api";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
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

function createForm(
  formDetail: ICreateStoreRequest,
  imageFile: File | string
): FormData {
  const formData = new FormData();

  const blob = new Blob([JSON.stringify(formDetail)], {
    type: "application/json"
  });

  formData.append("createStoreRequest", blob);
  formData.append("multipartFile", imageFile);

  return formData;
}

function ShopCompNew() {
  const jwt = useRecoilValue(authAtom);
  const setModalState = useSetRecoilState(modalStateAtom);

  const [loading, setLoading] = useState(false);
  const inputFile = useRef<HTMLInputElement | null>(null);
  const [shopNumber, setShopNumber] = useState("");
  const [shopName, setShopName] = useState("");
  const [shopAddress, setShopAddress] = useState("");
  const [imageFile, setImageFile] = useState<File | string>("");
  const [imageFileName, setImageFileName] =
    useState("사용가능한 확장자 : jpeg");

  return (
    <Box sx={style}>
      <Typography id="modal-modal-title" variant="h6" component="h2">
        가맹점 추가
      </Typography>
      <Grid container spacing={1} sx={{ mt: 2 }}>
        <Grid item xs={6}>
          <TextField
            size="small"
            sx={{ mt: 1 }}
            label="이름"
            variant="outlined"
            onChange={(event) => setShopName(event.target.value)}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            size="small"
            sx={{ mt: 1 }}
            label="전화번호"
            variant="outlined"
            onChange={(event) => setShopNumber(event.target.value)}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            fullWidth
            size="small"
            sx={{ mt: 1 }}
            label="상세설명 url"
            variant="outlined"
            onChange={(event) => setShopAddress(event.target.value)}
          />
        </Grid>
        <Grid item xs={5} sx={{ mt: 1 }}>
          <Button component="label" variant="outlined">
            <Typography>이미지 선택</Typography>
            <input
              hidden
              type="file"
              ref={inputFile}
              accept="image/jpeg"
              onChange={(event: ChangeEvent<HTMLInputElement>) => {
                if (!event.target.files) {
                  console.log("input Error");
                  return;
                } else {
                  setImageFile(event.target.files[0]);
                  setImageFileName(event.target.files[0].name);
                }
              }}
            />
          </Button>
        </Grid>
        <Grid item xs={7}>
          <Typography sx={{ mt: 2 }} fontSize={"small"}>
            {imageFileName}
          </Typography>
        </Grid>
      </Grid>
      <Box display="flex">
        <LoadingButton
          loading={loading}
          variant="contained"
          sx={{ ml: "auto", mt: 2 }}
          onClick={() => {
            setLoading((prevState) => !prevState);
            fetchCreateStore(
              jwt.accessToken,
              createForm(
                {
                  storeName: shopName,
                  address: shopAddress,
                  phoneNumber: shopNumber
                },
                imageFile
              )
            )
              .then((response) => {
                setLoading((prevState) => !prevState);
                setModalState((prevState) => !prevState);
                alert("생성 성공");
              })
              .catch((e) => {
                alert(e.response.data.message);
                setModalState((prevState) => !prevState);
              });
          }}
        >
          가맹점 추가
        </LoadingButton>
      </Box>
    </Box>
  );
}

export default ShopCompNew;
