#!/bin/zsh

BASE="${PWD}"
RESOURCE_PACK="${BASE}/resource-pack"
OUTPUT="${BASE}/build"
WORK="${OUTPUT}/tmp/resourcePack"
HASH_OUTPUT="${OUTPUT}/resource-pack.sha1"
ZIP_OUTPUT="${OUTPUT}/resource-pack.zip"

mkdir -p "${OUTPUT}"

# Cleanup
rm -rf "${WORK}"
mkdir -p "${WORK}"
rm -f "${HASH_OUTPUT}"
rm -f "${ZIP_OUTPUT}"

cp -r "${RESOURCE_PACK}/"* "${WORK}"
pngquant -vf --skip-if-larger --strip --ext .png "${WORK}"/**/*.png

# cd: zip only the contents of the resource pack folder, not the folder itself
cd "${WORK}" && \
zip -r "${ZIP_OUTPUT}" . -x '*.kra' '*.xcf' '*~' && \
cd "${BASE}" || exit 1

echo "Generating SHA-1..."
sha1sum "${ZIP_OUTPUT}" | awk '{ print $1 }' | tr -d '\n' >"${HASH_OUTPUT}"
echo "Done!"