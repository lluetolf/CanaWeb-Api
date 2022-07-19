# Set Project ID
1. gcloud projects list
## DEV: dev-canaweb-firestore
## PROD: prod-canaweb-firestore

# Deploy
project_id=dev-canaweb-firestore
gcloud app deploy --project=${project_id}
gcloud app deploy dispatch.yaml --project=${project_id}

