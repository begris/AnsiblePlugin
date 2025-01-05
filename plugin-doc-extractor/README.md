

ansible-doc -lj | yq 'keys[] | "\"" + . + "\"" ' | xargs -P 8 -i sh -c 'echo "Retrieving plugin {}" ;  ansible-doc -j {} > pl-list/{}.json | '

jq -n ' [inputs] | add ' pl-list/* > plugins.json

mv plugins.json ~/wrk/dev-oss/AnsiblePlugin/plugin-doc-extractor/src/main/resources/

run ExtractDoc -> creates Docs & plugin list

cp ~/wrk/dev-oss/AnsiblePlugin/docs/* ~/wrk/dev-oss/AnsiblePlugin/src/main/resources/docs
cp ~/wrk/dev-oss/AnsiblePlugin/plugins.json ~/wrk/dev-oss/AnsiblePlugin/src/main/resources/ 


