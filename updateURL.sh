export ORGANISATION="NclRSE-Training"
export SLUGS=`sqlite3 /home/jannetta/ExpanDrive/Sharepoint/Documents/General/RSE_HPC/Training/data.sqlite 'select slug from workshops WHERE slug > "2025-07-22-NCL"'`
for SLUG in $SLUGS
do
  gh repo edit ${ORGANISATION}/${SLUG} --homepage "https://${ORGANISATION}.github.io/${SLUG}"
done
