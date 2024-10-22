import http from '@/utils/httpRequest.js'
export function policy(pic) {
   return  new Promise((resolve,reject)=>{
        http({
            url: http.adornUrl("/thirdparty/minio/policy"),
            method: "get",
            params: http.adornParams({pic})
        }).then(({ data }) => {
            resolve(data);
        })
    });
}