# Ansible plugin manual extraction process

```shell
ansible-doc -lj | jq 'keys[] | .' | xargs -P 16 -i sh -c "echo 'Retrieving plugin {}' ; \
  ansible-doc -j {} > pl-list/{}.json" ; jq -n ' [inputs] | add' pl-list/* > ansible-plugins/plugins-source.json
```
