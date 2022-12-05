import * as React from "react";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import Link from "@mui/material/Link";

function Copyright() {
  return (
    <Typography variant="body2" color="text.secondary">
      {"Copyright © "}
      <Link color="inherit" href="https://www.hanbat.ac.kr/">
        HANBAT NATIONAL UNIVERSITY. ALL RIGHT RESERVED
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

export default function LoginFooter() {
  return (
    <Box
      component="footer"
      sx={{
        minWidth: "100vw",
        py: 1,
        backgroundColor: (theme) =>
          theme.palette.mode === "light"
            ? theme.palette.grey[200]
            : theme.palette.grey[800]
      }}
    >
      <Container>
        <Typography variant="body1">
          유성덕명캠퍼스 | 34158 대전광역시 유성구 동서대로 125 (덕명동)
          TEL.042-821-1141 FAX.042-825-5395
        </Typography>
        <Typography variant="body1">
          대덕산학융합캠퍼스 | 34014 대전광역시 유성구 테크노1로 75 (관평동)
          TEL.042-939-4701 FAX.042-939-4705
        </Typography>
        <Copyright />
      </Container>
    </Box>
  );
}
