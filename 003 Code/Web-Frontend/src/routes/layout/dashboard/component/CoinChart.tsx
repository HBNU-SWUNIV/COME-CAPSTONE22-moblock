import ApexChart from "react-apexcharts";
import { useRecoilValue } from "recoil";
import { authAtom, transactionAtom } from "../../../../atoms";
import { useQuery } from "react-query";
import { fetchCoinUsage } from "../../../../api";

interface ICoinUsage {
  tradingDate: string;
  tradingVolume: number;
}

type props = {
  coinName: string;
};

function CoinChart({ coinName }: props) {
  const jwt = useRecoilValue(authAtom);
  const systemDate = useRecoilValue(transactionAtom);
  const { isLoading, data } = useQuery<ICoinUsage[]>(
    ["coinUsage", coinName],
    async () =>
      await fetchCoinUsage(
        jwt.accessToken,
        coinName,
        systemDate.fromLocalDateTime,
        systemDate.untilLocalDateTime
      ).then((response) => response.data)
  );

  return isLoading ? (
    <span>loading...</span>
  ) : (
    <ApexChart
      type="area"
      series={[
        {
          name: "VALUE",
          data: data?.map(
            (usage) => usage.tradingVolume
          ) as unknown as number[]
        }
      ]}
      options={{
        chart: {
          // height: 350,
          zoom: {
            autoScaleYaxis: true
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: "straight"
        },

        title: {
          text: `usage of ${coinName?.toUpperCase()} coin`,
          align: "left"
        },
        subtitle: {
          text: "usage Movements",
          align: "left"
        },
        labels: data?.map((time) => time.tradingDate) as unknown as string[],
        xaxis: {
          type: "datetime"
        },
        yaxis: {
          opposite: true
        },
        legend: {
          horizontalAlign: "left"
        }
      }}
    />
  );
}

export default CoinChart;
