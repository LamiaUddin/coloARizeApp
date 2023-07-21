import type { Frame } from 'react-native-vision-camera'

export function simColourBlind(frame: Frame): Frame {
  'worklet'
  return __simColourBlind(frame)
}
