# App Router Data Mapping Steps


## step1 generate input json from exchange
1. get Body & Headers  
   `exchange.getIn().getBody`  
   `exchange.getIn().getHeader`
2. transform to json

**example**  
```json
{
   "header": {
      "Device-Id": "*"
   },
   "body": {
      "param": {
         "param1": "*"
      }
   }
}
```
## step2 generate output json by spec
1. definition input dto  
```json
{
   "header": {},
   "body": {
      "timestamp": "*",
      "sign": "*"
   }
}
```
2. definition out dto  
```json
{
   "header": {
      "timestamp": "$body.timestamp",
      "signType": "cfsSign",
      "sign": "$body.sign"
   },
   "body": {}
}
```
3. definition spec  
```json
[
   {
      "operation": "shift",
      "spec": {
         "body": {
            "timestamp": "header.timestamp",
            "sign": "header.sign"
         }
      }
   },
   {
      "operation": "default",
      "spec": {
         "header": {
            "signType": "cfsSign"
         },
         "body": {}
      }
   }
]
```

**example**   
```json
{
   "header": {
      "guid": "guid",
      "cycherId": "cycherId"
   },
   "body": {
      "refreshToken": "ZmRzYWZkc2FmZHNhZg==",
      "vin": "LVSHFCAC012341234",
      "sign": "sign"
   }
}
```
## step3 update exchange by json
1. update josn.body to exchange body  
   `exchange.setIn().setBody({...})`  
   **example**  
   `exchange.setIn().setBody("{\"refreshToken\":\"ZmRzYWZkc2FmZHNhZg==\",\"vin\":\"LVSHFCAC012341234\",\"sign\":\"sign\"}");`
2. update josn.heard to exchange heard  
   `exchange.setIn().setHeader(key,value)`  
   **example**  
   `
      exchange.setIn().setHeader("guid","guid");
      exchange.setIn().setHeader("cycherId","cycherId");
   `
# App Router Data Mapping Data


## API feedback context upload
### spec_feedback_data_flag
1. definition input dto
```json
{
   "header": {},
   "body": {
      "timestamp": "*",
      "sign": "*"
   }
}
```

2. out dto  
```json
{
   "header": {
      "timestamp": "$body.timestamp",
      "signType": "cfsSign",
      "sign": "$body.sign"
   },
   "body": {}
}
```
3. definition spec  
```json
[
   {
      "operation": "shift",
      "spec": {
         "body": {
            "timestamp": "header.timestamp",
            "sign": "header.sign"
         }
      }
   },
   {
      "operation": "default",
      "spec": {
         "header": {
            "signType": "cfsSign"
         },
         "body": {}
      }
   }
]
```
### spec_feedback_data_cipher_constant
1. definition input dto  
```json
{
   "header": {
      "GW-cipherStrategy" : "*"
   },
   "body": {}
}
```
2. out dto  
```json
{
   "header" : {
      "postCodeType" : "encodeBase64",
      "sharding" : "$header.GW-cipherStrategy",
      "preCodeType" : "decodeBase64",
      "saltType" : "KEYL16R",
      "keyType" : "communicationKey",
      "algorithm" : "AES/CBC/PKCS5Padding"
   },
   "body" : { }
}
```
3. definition spec  
```json
[
   {
      "operation": "default",
      "spec": {
         "header": {
            "algorithm": "AES/CBC/PKCS5Padding",
            "saltType": "KEYL16R",
            "keyType": "communicationKey",
            "sharding": "$header.GW-cipherStrategy",
            "preCodeType": "decodeBase64",
            "postCodeType": "encodeBase64"
         }
      }
   }
]
```
### spec_feedback_data_decrypt
1. definition input dto  
```json
{
   "header": {
      "Device-Id": "*"
   },
   "body": {
      "param": {
         "param1": "*"
      }
   }
}
```
2. out dto  
```json
{
   "header": {
      "Device-Id": "*",
      "cipherParam":"$body.param"
   },
   "body": {
      "param": "*"
   }
}
```
3. definition spec  
```json
[
   {
      "operation": "shift",
      "spec": {
         "header": {
            "Device-Id": "header.Device-Id",
            "cipherParam": "body.param"
         },
         "body": {
            "@": "&0",
            "param": "header.cipherParam"
         }
      }
   }
]
```
### spec_feedback_data_decrypt_openid
1. definition input dto  
```json
{
   "header": {
      "Device-Id": "*",
      "Open-Id": "*"
   },
   "body": {}
}
```
2. out dto  
```json
{
   "header" : {
      "Device-Id" : "*",
      "cipherParam" : "*"
   },
   "body" : { }
}
```
3. definition spec  
```json
[
   {
      "operation": "default",
      "spec": {
         "header": {
            "Device-Id": "header.Device-Id",
            "Open-Id": "header.cipherParam"
         }
      }
   }
]

```
### spec_feedback_context_cfs_call
1. definition input dto  
```json
{
   "header": {
      "timestamp": "*",
      "feedbackId": "*",
      "guid": "*",
      "vin": "*",
      "tempParam": {
         "osVersion": "*",
         "description": "*"
      }
   },
   "body": {
      "param": "*"
   }
}
```
2. out dto  
```json
{
   "header" : {
      "feedbackId" : "*"
   },
   "body" : {
      "guid" : "*",
      "vin" : "*",
      "osVersion" : "$header.tempParam.osVersion",
      "description" : "$header.tempParam.description",
      "submitTime" : "$header.timestamp"
   }
}
```
3. definition spec  
```json
[
   {
      "operation": "shift",
      "spec": {
         "header": {
            "feedbackId": "header.feedbackId",
            "guid": "body.guid",
            "vin": "body.vin",
            "tempParam": {
               "osVersion": "body.osVersion",
               "description": "body.description"
            },
            "timestamp": "body.submitTime"
         }
      }
   }
]
```