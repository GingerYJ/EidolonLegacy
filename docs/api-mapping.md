# API Mapping Notes

| Modern Forge API | 1.12.2 Forge/Cleanroom API |
| --- | --- |
| `Level` | `World` |
| `BlockState` | `IBlockState` |
| `BlockEntity` | `TileEntity` |
| `EntityType` | `EntityRegistry.registerModEntity` |
| `MobEffect` | `Potion` |
| `MobEffectInstance` | `PotionEffect` |
| `Menu` / `AbstractContainerMenu` | `Container` |
| `Screen` | `GuiScreen` / `GuiContainer` |
| `Component` | `ITextComponent` / `TextComponentTranslation` |
| `CompoundTag` | `NBTTagCompound` |
| `DeferredRegister` | `RegistryEvent.Register<T>` |
| `RegistryObject<T>` | Static registry fields |
| `SimpleChannel` | `SimpleNetworkWrapper` |

Add concrete replacements here as each source package is moved.
