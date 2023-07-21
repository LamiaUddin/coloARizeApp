/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */
import 'react-native-reanimated';
import React from 'react';

import {useEffect} from 'react';
import {Linking, Text, StyleSheet} from 'react-native';
import {
  Camera,
  useCameraDevices,
  useFrameProcessor,
} from 'react-native-vision-camera';
import { simColourBlind } from './frameProcessorPlugin';

const App = () => {
  const frameProcessor = useFrameProcessor(frame => {
    'worklet';
    const test = simColourBlind(frame);
    console.log(test);
    console.log("should return");

    return test;
  }, []);

  useEffect(() => {
    async function getPermission() {
      var cameraPermission = await Camera.getCameraPermissionStatus();

      if (cameraPermission == 'not-determined') {
        cameraPermission = await Camera.requestCameraPermission();
      }

      console.log('Camera permission status:', cameraPermission);

      if (cameraPermission == 'denied') {
        await Linking.openSettings();
      }
    }
    getPermission();
  }, []);

  const devices = useCameraDevices();
  const device = devices.back;

  if (device == null) return <Text>ColoArize</Text>;

  return (
    <Camera
      style={StyleSheet.absoluteFill}
      device={device}
      isActive={true}
      frameProcessor={frameProcessor}
      frameProcessorFps={1}
    />
  );
};

export default App;
