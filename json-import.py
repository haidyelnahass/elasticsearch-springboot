import sys
import json
from pprint import pprint
from elasticsearch import Elasticsearch
# from elasticsearch import RequestsHttpConnection
# _es2 = Elasticsearch([self.host], port=self.port, connection_class=RequestsHttpConnection, http_auth=(self.user, self.password), )
es = Elasticsearch(
    ['https://localhost:9200'],
    basic_auth=('elastic', 'ztMQpn1HhAubeKCqlMDi'),
    verify_certs=False,

)

MyFile= open(r"C:\Users\hamostafa\downloads\data.json","r").read()
print("File opened")
ClearData = MyFile.splitlines(True)
i=0
json_str=""
docs ={}
print("Now entering loop!")
i = 0
for line in ClearData:
    json_str=''
    i = i + 1
    line = ''.join(line.split())
    if line != "":
        if line[0] != ",":
            json_str = json_str+line
        else:
            json_str = json_str+line[1:]
        print("Adding record " , i)
        es.index(index='eai_dump', id=i, document=json_str)

print("done!");