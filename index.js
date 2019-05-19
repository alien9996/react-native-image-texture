
import { NativeModules } from 'react-native';

const { RNImageTexture } = NativeModules;

const DEFAULT_OPTIONS = {
    imageSource1: null,
    imageSource2: null,
    dataType1: "Path",
    dataType2: "Path",
    textureType: 0,
    isAccsets: true,
    mode: 3
}

module.exports = {
    ...RNImageTexture,
    getResourcesImageTexture: function getResourcesImageTexture(options, callback) {
        if (typeof options === 'function') {
            callback = options;
            options = {};
        }
        return RNImageTexture.getResourcesImageTexture({ ...DEFAULT_OPTIONS, ...options }, callback)
    }
}
