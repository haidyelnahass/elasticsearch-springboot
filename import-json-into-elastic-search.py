import json
from elasticsearch import Elasticsearch, helpers
from datetime import datetime
###########################################################################
# -------------- IMPORT JSON FROM ORACLE INTO ELASTIC SEARCH -------------#
###########################################################################

# For HTTP elasticsearch cluster
# es = Elasticsearch(
#     ['http://localhost:9200'],
# )

# For HTTPS elastic search cluster
es = Elasticsearch(
    ['https://localhost:9200'],
    basic_auth=('elastic', 'j_yk8UP3BG-F=F1ekXli'), # add your elastic cluster credentials here
    verify_certs=False,

)
bulk_doc_list = []
# Add path to your JSON file here
print("started")
now = datetime.now()
current_time = now.strftime("%H:%M:%S")
print("Current Time =", current_time)
with open(r"C:\Users\hamostafa\downloads\tmstmp.json") as json_file:
    data = json.load(json_file)
    item_list = data['results'][0]['items']
    items_len = len(item_list)
    print(items_len)
    for id in range(0, items_len, 100000):
        print("Now inserting from document number ", id)
        helpers.bulk(es, item_list[id:id+100000], index='eai-dump-table') # Add preferred index name 
print("done")
now = datetime.now()
current_time = now.strftime("%H:%M:%S")
print("Current Time =", current_time)
