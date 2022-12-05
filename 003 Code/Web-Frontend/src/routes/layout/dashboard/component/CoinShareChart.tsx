import ApexChart from "react-apexcharts";
import { ICoinDtoList } from "../../../../interfaces";

type props = {
  coinList: ICoinDtoList[];
  totalIssuance: number;
};

function CoinShareChart({ coinList, totalIssuance }: props) {

  return (
    <ApexChart
      type="pie"
      series={coinList.map((element) => element.issuance)}
      options={{
        chart: {
          // height: 300,
          zoom: {
            // autoScaleYaxis: true
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: "straight"
        },

        title: {
          text: `COIN SHARE`,
          align: "left"
        },
        labels: coinList.map((element) => element.name)
      }}
    />
  );
}

export default CoinShareChart;

